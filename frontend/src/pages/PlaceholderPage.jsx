function PlaceholderPage({
  title,
  description
}) {
  return (
    <div className="page">

      <div className="page-heading">

        <h1>
          {title}
        </h1>

        <p>
          {description}
        </p>

      </div>

    </div>
  );
}

export default PlaceholderPage;