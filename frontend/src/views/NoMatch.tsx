import { FC } from 'react'
import { Link } from 'react-router-dom'

interface NoMatchProps {

}

export const NoMatch: FC<NoMatchProps> = () => {

    return (
        <div>
            <h1>Nothing to see here!</h1>
            <p>
                <Link to="/">Go to the home page</Link>
            </p>
        </div>
    )
}
