async function get1(bno) {
    const result = await axios.get(`/replies/list/${bno}`)
    //console.log(result)
    return result;
}

/*댓글 목록 처리*/
async function getList({bno, page, size, goLast}){

    const result = await axios.get(`/replies/list/${bno}`, {params: {page, size}})

    if(goLast){
        const total = result.data.total
        const lastPage = parseInt(Math.ceil(total/size))

        return getList({bno:bno, page:lastPage, size:size})

    }

    return result.data
}

/*댓글 등록*/
async function addReply(replyObj) {
    const response = await axios.post(`/replies/`,replyObj)
    return response.data
}